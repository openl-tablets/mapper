package org.openl.rules.mapping;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.syntax.exception.CompositeOpenlException;

public class ValidationTest {

    @Test
    public void fieldPathHierarchyValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/fieldpath/FieldPathValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(6, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

    @Test
    public void convertMethodValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/customconverter/ConvertMethodValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(2, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

    @Test
    public void conditionMethodValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/condition/ConditionMethodValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(2, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

    @Test
    public void discriminatorMethodValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/discriminator/DiscriminatorMethodValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(0, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

    @Test
    public void discriminatorMethodNotFoundValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/discriminator/DiscriminatorMethodNotFoundValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(1, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

    @Test
    public void discriminatorMethodTargetFieldValidationTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/validation/discriminator/DiscriminatorMethodTargetFieldValidationTest.xlsx");
        try {
            RulesBeanMapperFactory.createMapperInstance(source, null, null, false);
        } catch (RulesMappingException ex) {
            assertEquals(1, ((CompositeOpenlException) ex.getCause()).getErrorMessages().length);
        }
    }

}
