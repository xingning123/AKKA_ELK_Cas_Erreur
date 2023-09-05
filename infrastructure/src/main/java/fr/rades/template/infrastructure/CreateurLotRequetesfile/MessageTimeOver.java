package fr.rades.template.infrastructure.CreateurLotRequetesfile;

import akka.dispatch.ControlMessage;

public class MessageTimeOver implements ControlMessage {
    public static final MessageTimeOver Instance = new MessageTimeOver();
}
