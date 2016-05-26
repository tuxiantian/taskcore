package com.ai.taskcore.quartz.impl;

import java.io.Serializable;

public interface Job extends Serializable {
	void execute();
}
