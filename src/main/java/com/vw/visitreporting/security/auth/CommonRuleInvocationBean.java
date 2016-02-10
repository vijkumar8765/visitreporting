package com.vw.visitreporting.security.auth;

import java.util.Set;

import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;

public class CommonRuleInvocationBean {

	private Set<AccessControlRule> commonRulesToBeApplied;

	public boolean applyCommonAccessControlRules(Object entity,
			Function function) {

		for (AccessControlRule aRule : commonRulesToBeApplied) {
			if (aRule.supports(entity)) {
				Boolean allowed = aRule.invokeRule(entity, function);
				if (allowed != null && (!allowed)) {
					return false;
				}
			}
		}

		return true;
	}

	public Set<AccessControlRule> getCommonRulesToBeApplied() {
		return commonRulesToBeApplied;
	}

	public void setCommonRulesToBeApplied(
			Set<AccessControlRule> commonRulesToBeApplied) {
		this.commonRulesToBeApplied = commonRulesToBeApplied;
	}

}
