package views

import java.time.LocalDate

import forms.$className$FormProvider
import models.NormalMode
import nunjucks.viewmodels.DateViewModel
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.QuestionViewBehaviours
import views.html.$className;format="decap"$View
import nunjucks.$className$Template

class $className;format="decap"$ViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className$FormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"$className $ (\$templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render($className$Template, Json.toJsObject(DateViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[$className;format="decap"$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))
    }
  }
}
