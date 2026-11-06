package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.image.ImageFile;

import java.util.Date;

public record CreateEventCommand(String title,
                                 String description,
                                 Date date,
                                 String city,
                                 String state,
                                 Boolean remote,
                                 String eventUrl,
                                 ImageFile image) {}
