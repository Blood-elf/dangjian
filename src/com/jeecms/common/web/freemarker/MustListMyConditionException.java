package com.jeecms.common.web.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 非List<MyCondition>参数异常
 */
@SuppressWarnings("serial")
public class MustListMyConditionException extends TemplateModelException {
	public MustListMyConditionException(String paramName) {
		super("The \"" + paramName + "\" parameter must be a List<MyCondition> with NOTNULL Field.");
	}
}