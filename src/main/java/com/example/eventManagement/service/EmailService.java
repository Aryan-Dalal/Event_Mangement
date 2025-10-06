package com.example.eventManagement.service;

import com.example.eventManagement.entity.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Send approval email with emojis and price details
    public void sendApprovalEmail(Registration registration) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(registration.getUser().getEmail());
        message.setSubject("🎉 Your Sangam Event Booking Has Been Approved! ✅");

        String userName = registration.getUser() != null && registration.getUser().getName() != null
                ? registration.getUser().getName() : "Guest";
        String eventName = registration.getEvent() != null && registration.getEvent().getName() != null
                ? registration.getEvent().getName() : "Event";
        String startDate = registration.getStartDate() != null ? registration.getStartDate().toString() : "N/A";
        String endDate = registration.getEndDate() != null ? registration.getEndDate().toString() : "N/A";
        String location = registration.getEvent() != null && registration.getEvent().getLocation() != null
                ? registration.getEvent().getLocation() : "N/A";
        String address = registration.getEvent() != null && registration.getEvent().getAddress() != null
                ? registration.getEvent().getAddress() : "N/A";

        // ✅ Veg/Non-Veg and price info
        String vegInfo = registration.getVegSelected() != null && registration.getVegSelected()
                ? "Veg Selected: ₹" + registration.getVegPrice()
                : "Veg Not Selected";
        String nonVegInfo = registration.getNonvegSelected() != null && registration.getNonvegSelected()
                ? "Non-Veg Selected: ₹" + registration.getNonvegPrice()
                : "Non-Veg Not Selected";
        String totalPriceInfo = "Total Price: ₹" + registration.getTotalPrice();

        String body = String.format(
                "👋 Hello %s,\n\n" +
                        "Your booking for '%s' has been APPROVED! 🎊\n\n" +
                        "📝 Booking Details:\n" +
                        "----------------------------------------------------\n" +
                        "🎟️ Booking ID: %d\n" +
                        "📅 Dates: %s to %s\n" +
                        "📍 Location: %s\n" +
                        "🏢 Address: %s\n" +
                        "🥗 %s\n" +
                        "🍖 %s\n" +
                        "💰 %s\n" +
                        "----------------------------------------------------\n\n" +
                        "We look forward to hosting you! 🤗\n\n" +
                        "Warm regards,\n" +
                        "The Sangam Events Team 💐\n" +
                        "📧 sangamevents80@gmail.com\n" +
                        "🌐 www.sangamevents.com",
                userName,
                eventName,
                registration.getRegistrationId(),
                startDate,
                endDate,
                location,
                address,
                vegInfo,
                nonVegInfo,
                totalPriceInfo
        );

        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("✅ Approval email SENT to: " + registration.getUser().getEmail());
        } catch (Exception e) {
            System.err.println("❌ Approval email FAILED for: " + registration.getUser().getEmail());
            e.printStackTrace();
        }
    }

    // ✅ Send rejection email (unchanged, can add veg info if needed)
    public void sendRejectionEmail(Registration registration) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(registration.getUser().getEmail());
        message.setSubject("❌ Update: Your Sangam Event Booking Was Rejected 😔");

        String userName = registration.getUser().getName() != null ? registration.getUser().getName() : "Guest";
        String eventName = registration.getEvent().getName() != null ? registration.getEvent().getName() : "Event";
        String startDate = registration.getStartDate() != null ? registration.getStartDate().toString() : "N/A";
        String endDate = registration.getEndDate() != null ? registration.getEndDate().toString() : "N/A";
        String location = registration.getEvent().getLocation() != null ? registration.getEvent().getLocation() : "N/A";
        String address = registration.getEvent().getAddress() != null ? registration.getEvent().getAddress() : "N/A";

        String body = String.format(
                "👋 Hello %s,\n\n" +
                        "We’re sorry to inform you that your booking for '%s' has been rejected. ❌\n\n" +
                        "📝 Booking Details:\n" +
                        "----------------------------------------------------\n" +
                        "🎟️ Booking ID: %d\n" +
                        "📅 Dates: %s to %s\n" +
                        "📍 Location: %s\n" +
                        "🏢 Address: %s\n" +
                        "----------------------------------------------------\n\n" +
                        "Please try booking for different dates or contact our support team. 🤝\n\n" +
                        "Warm regards,\n" +
                        "The Sangam Events Team 💐\n" +
                        "📧 sangamevents80@gmail.com\n",
                userName,
                eventName,
                registration.getRegistrationId(),
                startDate,
                endDate,
                location,
                address
        );

        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("✅ Rejection email SENT to: " + registration.getUser().getEmail());
        } catch (Exception e) {
            System.err.println("❌ Rejection email FAILED for: " + registration.getUser().getEmail());
            e.printStackTrace();
        }
    }
}
