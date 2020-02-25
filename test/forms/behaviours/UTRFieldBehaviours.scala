package forms.behaviours

import play.api.data.{Form, FormError}

trait UTRFieldBehaviours extends FieldBehaviours {

  def validUTR(
                form: Form[_],
                utrFieldName: String = "value",
                validUTR: String = "1111111111",
                invalidUTR: String = "1234567899",
                validRegexp: String = "^[0-9]{10}$",
                invalidRegexp: String = "1234",
                utrChecksumErrorKey: String,
                utrRegexpKey: String
              ): Unit = {
    s"$utrFieldName" must {

      "when binding a value which does match the regexp" when {

        "checksum fails" should {

          s"return the checksum error for $utrFieldName" in {
            val result = form.bind(Map(utrFieldName -> invalidUTR)).apply(utrFieldName)
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrChecksumErrorKey))
          }
        }

        "checksum is successful" should {

          "return the field value" in {
            val result = form.bind(Map(utrFieldName -> validRegexp)).apply(utrFieldName)
            result.value mustBe Some(validRegexp)
          }
        }

        "when binding a value which does not match the regexp" should {

          "return the regexp error" in {
            val result = form.bind(Map(utrFieldName -> invalidRegexp)).apply(utrFieldName)
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrRegexpKey, Seq(validRegexp)))
          }
        }
      }
    }
  }
}

