package forms.$section;format="decap"$

import forms.behaviours.DecimalFieldBehaviours
import play.api.data.FormError

class $className;format="cap"$FormProviderSpec extends DecimalFieldBehaviours {

  val form = new $className;format="cap"$FormProvider()()

  ".value" must {

    val fieldName = "value"

    val minimum = $minimum$
    val maximum = $maximum$

    val validDataGenerator = decimalInRangeWithCommas(minimum, maximum)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like decimalField(
      form,
      fieldName,
      nonNumericError  = FormError(fieldName, "$className;format="decap"$.error.nonNumeric"),
      invalidNumericError = FormError(fieldName, "$className;format="decap"$.error.invalidNumeric")
    )

    behave like decimalFieldWithRange(
      form,
      fieldName,
      minimum       = minimum,
      maximum       = maximum,
      expectedError = FormError(fieldName, "$className;format="decap"$.error.outOfRange", Seq(minimum, maximum))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "$className;format="decap"$.error.required")
    )
  }
}
