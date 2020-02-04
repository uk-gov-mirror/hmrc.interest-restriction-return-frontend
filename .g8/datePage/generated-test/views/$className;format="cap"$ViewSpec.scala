package views

import java.time.LocalDate
import forms.$className;format="cap"$FormProvider
import models.NormalMode
import play.api.data.Form

import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.QuestionViewBehaviours
import views.html.$className;format="cap"$View

class $className;format="cap"$ViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className;format="cap"$FormProvider()()

    s"$className;format="cap"$View" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))
    }
  }
}
