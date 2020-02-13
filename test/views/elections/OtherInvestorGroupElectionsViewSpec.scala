package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.elections.OtherInvestorGroupElectionsFormProvider
import models.{OtherInvestorGroupElections, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.elections.OtherInvestorGroupElectionsView
import controllers.elections.routes.OtherInvestorGroupElectionsController

class OtherInvestorGroupElectionsViewSpec extends CheckboxViewBehaviours[OtherInvestorGroupElections] {

  val messageKeyPrefix = "otherInvestorGroupElections"
  val section = Some(messages("section.elections"))
  val form = new OtherInvestorGroupElectionsFormProvider()()

    "OtherInvestorGroupElectionsView" must {

      val view = viewFor[OtherInvestorGroupElectionsView](Some(emptyUserAnswers))

      def applyView(form: Form[Set[OtherInvestorGroupElections]]): HtmlFormat.Appendable = {
        val view = viewFor[OtherInvestorGroupElectionsView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like checkboxPage(form, applyView, messageKeyPrefix, OtherInvestorGroupElections.options(form), messages("section.elections"))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
  }
}
