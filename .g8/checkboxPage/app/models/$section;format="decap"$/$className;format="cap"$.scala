package models
import pages.QuestionPage
import models.{Enumerable, WithName}
import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem

sealed trait $className;format="cap"$

object $className;format="cap"$ extends Enumerable.Implicits {

  case object $option1key;format="Camel"$ extends WithName("$option1key;format="decap"$") with $className;format="cap"$
  case object $option2key;format="Camel"$ extends WithName("$option2key;format="decap"$") with $className;format="cap"$

  val values: Seq[$className;format="cap"$] = Seq(
    $option1key;format="Camel"$, $option2key;format="Camel"$
  )

  def options(form: Form[_])(implicit messages: Messages): Seq[CheckboxItem] = values.map {
    value =>
      CheckboxItem(
        name = Some("value[]"),
        id = Some(value.toString),
        value = value.toString,
        content = Text(messages(s"$className;format="decap"$.\${value.toString}")),
        checked = form.data.exists(_._2 == value.toString)
    )
  }

  implicit val enumerable: Enumerable[$className;format="cap"$] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
