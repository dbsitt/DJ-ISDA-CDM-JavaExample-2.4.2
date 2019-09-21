package com.regnosys.cdm.example;

import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.lib.validation.Validator;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.meta.InterestRatePayoutMeta;
import org.isda.cdm.validation.datarule.InterestRatePayoutActualQuantityDataRule;

import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Illustration of how to invoke validations on a CDM object as well as
 * individual validations
 */
public class Validation {

	public static void main(String[] args) {
		InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout();

		// Recursively run all validators for an object
		//
		new RosettaTypeValidator()
				.runProcessStep(InterestRatePayout.class, fixedRatePayout.toBuilder())
				.getValidationResults().forEach(System.out::println);

		// The validators for a single class can be accessed via it's meta class
		//
		InterestRatePayoutMeta fixedRatePayoutMeta = (InterestRatePayoutMeta) fixedRatePayout.metaData();

		// the meta class offers granularity over which types of Validators to extract
		//
		List<Validator<? super InterestRatePayout>> validators = newArrayList(concat(fixedRatePayoutMeta.choiceRuleValidators(), fixedRatePayoutMeta.dataRules()));

		// fixedRatePayoutMeta.validator() returns the cardinality validator for fixedRatePayout
		//
		validators.add(fixedRatePayoutMeta.validator());

		// Run Validators
		//
		validators.stream()
				.map(validator -> validator.validate(RosettaPath.valueOf("InterestRatePayout"), fixedRatePayout))
				.sorted(Comparator.comparing(ValidationResult::isSuccess, Boolean::compareTo)) // failures first
				.forEach(System.out::println);

		// Individual Validators can be invoked for further debugging
		//
		ValidationResult<InterestRatePayout>  validationResult = new InterestRatePayoutActualQuantityDataRule()
				.validate(RosettaPath.valueOf("InterestRatePayout"), fixedRatePayout);

		System.out.println("\nSingle validation result:\n" + validationResult);
	}

}
