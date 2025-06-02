package com.namacmo.user.api.v1.level.adapter.in.saga.command;

import com.namacmo.infracommon.saga.CommandEvent;
import com.namacmo.user.api.v1.level.domain.model.MembershipLevel;

public interface MembershipCommandEvent extends CommandEvent<MembershipLevel> {
}
