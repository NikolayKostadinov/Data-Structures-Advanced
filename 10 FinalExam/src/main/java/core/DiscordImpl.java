package core;

import models.Message;

import java.util.*;
import java.util.stream.Collectors;

public class DiscordImpl implements Discord {
    private Map<String, Message> messagesById;

    //         Chanel ,     MessageId, Message
    private Map<String, Map<String,Message>> messagesByChanel;

    public DiscordImpl() {
        this.messagesById = new LinkedHashMap<>();
        this.messagesByChanel = new LinkedHashMap<>();
    }

    @Override
    public void sendMessage(Message message) {
        this.messagesById.put(message.getId(), message);
        this.messagesByChanel.computeIfAbsent(message.getChannel(), k->new LinkedHashMap<>())
                .put(message.getId(), message);
    }

    @Override
    public boolean contains(Message message) {
        return this.messagesById.containsKey(message.getId());
    }

    @Override
    public int size() {
        return this.messagesById.size();
    }

    @Override
    public Message getMessage(String messageId) {
        Message message = this.messagesById.get(messageId);
        if (message==null) throw new IllegalArgumentException();
        return message;
    }

    @Override
    public void deleteMessage(String messageId) {
        Message messageToRemove = this.messagesById.remove(messageId);
        if (messageToRemove == null) throw new IllegalArgumentException();
        this.messagesByChanel.get(messageToRemove.getChannel()).remove(messageToRemove.getId());
    }

    @Override
    public void reactToMessage(String messageId, String reaction) {
        this.getMessage(messageId).getReactions().add(reaction);
    }

    @Override
    public Iterable<Message> getChannelMessages(String channel) {
        Map<String, Message> messages = this.messagesByChanel.get(channel);
        if (messages == null || messages.isEmpty()) throw new IllegalArgumentException();
        return messages.values();
    }

    @Override
    public Iterable<Message> getMessagesByReactions(List<String> reactions) {
        return this.messagesById
                .values()
                .stream()
                .filter(message -> message.getReactions().containsAll(reactions))
                .sorted((m1,m2)->{
                    int comparison = Integer.compare(m2.getReactions().size(), m2.getReactions().size());
                    if (comparison == 0){
                        comparison = Integer.compare(m1.getTimestamp(), m2.getTimestamp());
                    }
                    return comparison;
                })
                .collect(Collectors.toList());
    }

    //todo: optimize by additional TreeMap By timestamp
    @Override
    public Iterable<Message> getMessageInTimeRange(Integer lowerBound, Integer upperBound) {
        return this.messagesById
                .values()
                .stream()
                .filter(message -> lowerBound <= message.getTimestamp() && message.getTimestamp() <= upperBound)
                .sorted((m1,m2) ->
                    Integer.compare(this.messagesByChanel.get(m2.getChannel()).size(),
                            this.messagesByChanel.get(m1.getChannel()).size())
                )
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getTop3MostReactedMessages() {
        return this.messagesById
                .values()
                .stream()
                .sorted((m1,m2)->Integer.compare(m2.getReactions().size(), m1.getReactions().size()))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Message> getAllMessagesOrderedByCountOfReactionsThenByTimestampThenByLengthOfContent() {
        return this.messagesById
                .values()
                .stream()
                .sorted((m1,m2)->{
                    int comparison = Integer.compare(m2.getReactions().size(), m1.getReactions().size());

                    if (comparison == 0){
                        comparison = Integer.compare(m1.getTimestamp(), m2.getTimestamp());
                    }

                    if (comparison == 0){
                        comparison = Integer.compare(m1.getContent().length(), m2.getContent().length());
                    }
                    return comparison;

                })
                .collect(Collectors.toList());
    }
}
