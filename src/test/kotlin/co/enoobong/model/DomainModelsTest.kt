package co.enoobong.model

import com.openpojo.validation.ValidatorBuilder
import com.openpojo.validation.rule.impl.GetterMustExistRule
import com.openpojo.validation.rule.impl.SetterMustExistRule
import com.openpojo.validation.test.impl.GetterTester
import com.openpojo.validation.test.impl.SetterTester
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test

class DomainModelsTest {

    @Test
    fun validateGettersAndSetters() {
        val validator = ValidatorBuilder.create()
            .with(SetterMustExistRule(), GetterMustExistRule())
            .with(SetterTester(), GetterTester())
            .build()
        val packageToValidate = "co.enoobong.model"
        validator.validate(packageToValidate)
    }

    @Test
    fun rideEqualsHashCodeContract() {
        EqualsVerifier.forClass(Ride::class.java)
            .usingGetClass()
            .verify()
    }


    @Test
    fun personEqualsHashCodeContract() {
        EqualsVerifier.forClass(Person::class.java)
            .withIgnoredFields("registrationDate")
            .usingGetClass()
            .verify()
    }

}