package views

import java.time.LocalDate

import forms.$className$FormProvider
import models.{NormalMode}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.$className$View

class $className$ViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "Legend Message Heading displayed for $className;format="decap"$ goes here"

  val form = new $className$FormProvider()()

  "$className$View view" must {

    val view = viewFor[$className$View](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))
  }
}
