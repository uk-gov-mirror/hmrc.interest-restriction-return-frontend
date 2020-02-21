package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.elections.routes
import forms.elections.InvestmentNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.elections.InvestmentNameView

class InvestmentNameViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "investmentName"
  val section = Some(messages("section.elections"))
  val form = new InvestmentNameFormProvider()()

    "InvestmentNameView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[InvestmentNameView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like stringPage(form, applyView, messageKeyPrefix, routes.InvestmentNameController.onSubmit(NormalMode).url, section = section)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
