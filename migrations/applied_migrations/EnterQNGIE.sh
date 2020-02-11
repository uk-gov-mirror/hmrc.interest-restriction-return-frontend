#!/bin/bash

echo ""
echo "Applying migration EnterQNGIE"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### EnterQNGIE Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "EnterQNGIE" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.EnterQNGIEController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.EnterQNGIEController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.EnterQNGIEController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.EnterQNGIEController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# EnterQNGIEPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "enterQNGIE.title = EnterQNGIE" >> ../conf/messages.en
echo "enterQNGIE.heading = EnterQNGIE" >> ../conf/messages.en
echo "enterQNGIE.label= Enter the qualifying net group-interest expense (QNGIE)" >> ../conf/messages.en
echo "enterQNGIE.checkYourAnswersLabel = EnterQNGIE" >> ../conf/messages.en
echo "enterQNGIE.error.nonNumeric = Enter your enterQNGIE using numbers" >> ../conf/messages.en
echo "enterQNGIE.error.required = Enter your enterQNGIE" >> ../conf/messages.en
echo "enterQNGIE.error.invalidNumeric = The enterQNGIE must be valid decimal or whole number" >> ../conf/messages.en
echo "enterQNGIE.error.outOfRange = EnterQNGIE must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# EnterQNGIEPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "enterQNGIE.title = EnterQNGIE" >> ../conf/messages.cy
echo "enterQNGIE.heading = EnterQNGIE" >> ../conf/messages.cy
echo "enterQNGIE.label=Enter the qualifying net group-interest expense (QNGIE)" >> ../conf/messages.cy
echo "enterQNGIE.checkYourAnswersLabel = EnterQNGIE" >> ../conf/messages.cy
echo "enterQNGIE.error.nonNumeric = Enter your enterQNGIE using numbers" >> ../conf/messages.cy
echo "enterQNGIE.error.required = Enter your enterQNGIE" >> ../conf/messages.cy
echo "enterQNGIE.error.invalidNumeric = The enterQNGIE must be valid decimal or whole number" >> ../conf/messages.cy
echo "enterQNGIE.error.outOfRange = EnterQNGIE must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterQNGIEUserAnswersEntry: Arbitrary[(EnterQNGIEPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EnterQNGIEPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterQNGIEPage: Arbitrary[EnterQNGIEPage.type] =";\
    print "    Arbitrary(EnterQNGIEPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EnterQNGIEPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def enterQNGIE: Option[SummaryListRow] = answer(EnterQNGIEPage, routes.EnterQNGIEController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    EnterQNGIEPage.toString -> EnterQNGIEPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    EnterQNGIEPage.toString -> EnterQNGIEPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val enterQNGIE = \"Enter the qualifying net group-interest expense (QNGIE)\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|Elections\/${kebabClassName}|g" ../generated-it/controllers/EnterQNGIEControllerISpec.scala

echo "Migration EnterQNGIE completed"
