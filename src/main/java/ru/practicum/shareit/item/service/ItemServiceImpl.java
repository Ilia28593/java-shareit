package ru.practicum.shareit.item.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.coment.model.Comment;
import ru.practicum.shareit.coment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.GetItemResponseDTO;
import ru.practicum.shareit.item.dto.ItemConvertorItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.CrudService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl extends CrudService<Item> implements ItemService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;

    private final ItemConvertorItemResponseDto convertor;

    @Override
    public GetItemResponseDTO getByIdFull(long userId, long itemId) {
        if (getById(itemId).getOwner().getId() == userId) {
            return getGetItemOwnerResponseDTO(itemId, userId);
        }
        return convertor.convertFromUpdate(super.getById(itemId), commentRepository.findAllByItemIdOrderByCreatedDesc(itemId));
    }

    @Override
    public Comment createComment(long userId, long itemId, Comment comment) {
        if (comment.getText().isBlank()) {
            throw new ValidationException(String.format("Exception from create comment from item id %n", itemId));
        }
        List<Booking> booking = bookingRepository.findAllByItemIdAndBookerIdAndBookingStatusAndEndBefore(itemId, userId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new ValidationException(String.format("Exception from create comment from item id %n", itemId));
        }
        comment.setAuthor(userService.getById(userId));
        comment.setItem(itemRepository.getById(itemId));
        Comment commentCreated = commentRepository.save(comment);
        return commentCreated;
    }

    private GetItemResponseDTO getGetItemOwnerResponseDTO(long itemId, long userId) {
        List<Booking> bookingByIdItemId = bookingRepository.findLastBookingByIdItemId(itemId);
        List<Booking> listNextBooking = bookingByIdItemId.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).sorted(Comparator.comparing(Booking::getStart)).collect(Collectors.toList());
        List<Booking> listLastBooking = bookingByIdItemId.stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now())).sorted(Comparator.comparing(Booking::getEnd).reversed()).collect(Collectors.toList());
        if (bookingByIdItemId.isEmpty()) {
            return convertor.convertFromUpdate(super.getById(itemId), commentRepository.findAllByItemIdOrderByCreatedDesc(itemId));
        } else if (bookingByIdItemId.size() == 1) {
            return convertor.convertToGetItemResponseDto(getById(itemId), listNextBooking, listLastBooking, commentRepository.findAllByItemIdOrderByCreatedDesc(itemId));
        } else {
            return convertor.convertToGetItemResponseDto(getById(itemId),
                    listNextBooking,
                    listLastBooking,
                    commentRepository.findAllByItemIdOrderByCreatedDesc(itemId));
        }
    }

    @Override
    public Item create(Long idUser, Item entity) {
        if (entity.getName().isBlank()) {
            throw new ValidationException(String.valueOf(idUser));
        }
        entity.setOwner(userService.getById(idUser));
        return super.create(entity);
    }

    @Override
    public Item update(long userId, long itemId, Item entity) {
        userService.validateIds(userId);
        validateIds(itemId);
        if (getById(itemId).getOwner().getId() == userId) {
            Item item = getById(itemId);
            item.setName(entity.getName() != null ? entity.getName() : item.getName());
            item.setRequest(entity.getRequest() != null ? entity.getRequest() : item.getRequest());
            item.setDescription(entity.getDescription() != null ? entity.getDescription() : item.getDescription());
            item.setAvailable(entity.getAvailable() != null ? entity.getAvailable() : item.getAvailable());
            return super.update(item);
        }
        throw new NotFoundException(String.valueOf(userId));
    }

    @Override
    public List<Item> getFromDescription(long userId, @NonNull String text) {
        userService.validateIds(userId);
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(text, text)
                .stream()
                .distinct()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
    @Override
    public List<GetItemResponseDTO> getAllByOwner(long userId) {
        return itemRepository.findAllByOwnerIdOrderById(userId).stream()
                .map(Item::getId)
                .map(i -> getGetItemOwnerResponseDTO(i, userId))
                .collect(Collectors.toList());
    }

    @Override
    protected String getServiceType() {
        return Item.class.getSimpleName();
    }

    @Override
    protected JpaRepository<Item, Long> getRepository() {
        return itemRepository;
    }
}
