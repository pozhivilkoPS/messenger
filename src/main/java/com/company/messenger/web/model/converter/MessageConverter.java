package com.company.messenger.web.model.converter;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.messenger.data.entity.Message;
import com.company.messenger.data.repository.ConversationRepository;
import com.company.messenger.data.repository.UserRepository;
import com.company.messenger.web.model.MessageWebModel;

@Component
public class MessageConverter {

    private UserRepository userRepository;

    private ConversationRepository conversationRepository;

    private UserConverter userConverter;

    @Autowired
    public MessageConverter(UserRepository userRepository, ConversationRepository conversationRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.userConverter = userConverter;
    }

    public MessageWebModel convert(Message message) {
        MessageWebModel model = new MessageWebModel();

        model.setId(message.getId());
        model.setText(message.getText());
        model.setDate(message.getDate());
        model.setUser(userConverter.convert(message.getUser()));
        model.setConversationId(message.getConversation().getId());

        return model;
    }

    public List<MessageWebModel> convert(Collection<Message> messageList) {
        return messageList.stream().map(this::convert).sorted(Comparator.comparing(MessageWebModel::getDate)).collect(Collectors.toList());
    }

    public Message convert(MessageWebModel model) {
        Message message = new Message();

        message.setId(model.getId());
        message.setText(model.getText());
        message.setDate(model.getDate());
        message.setUser(userRepository.findById(model.getUser().getId())
            .orElseThrow(() -> new IllegalArgumentException("wrong id of user")));
        message.setConversation(conversationRepository.findById(model.getConversationId())
            .orElseThrow(() -> new IllegalArgumentException("wrong id of conversation")));

        return message;
    }

}
