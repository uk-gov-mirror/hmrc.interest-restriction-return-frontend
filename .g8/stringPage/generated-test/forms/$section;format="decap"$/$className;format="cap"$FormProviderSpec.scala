package forms.$section;format="decap"$

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class $className;format="cap"$FormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "$className;format="decap"$.error.required"
  val lengthKey = "$className;format="decap"$.error.length"
  val maxLength = $maxLength$

  val form = new $className;format="cap"$FormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
