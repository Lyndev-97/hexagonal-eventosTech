package com.eventostec.api.application.usecases;

import java.util.Date;

public record AddCouponCommand(String code, Integer discount, Date valid) {}
