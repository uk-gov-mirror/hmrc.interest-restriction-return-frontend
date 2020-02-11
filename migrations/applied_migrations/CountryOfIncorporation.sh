#!/bin/bash

echo ""
echo "Applying migration CountryOfIncorporation"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### CountryOfIncorporation Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CountryOfIncorporation" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.groupStructure.CountryOfIncorporationController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.groupStructure.CountryOfIncorporationController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.groupStructure.CountryOfIncorporationController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.groupStructure.CountryOfIncorporationController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CountryOfIncorporationPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "countryOfIncorporation.title = countryOfIncorporation" >> ../conf/messages.en
echo "countryOfIncorporation.heading = countryOfIncorporation" >> ../conf/messages.en
echo "countryOfIncorporation.checkYourAnswersLabel = countryOfIncorporation" >> ../conf/messages.en
echo "countryOfIncorporation.label= CountryOfIncorporation" >> ../conf/messages.en
echo "countryOfIncorporation.error.required = Enter countryOfIncorporation" >> ../conf/messages.en
echo "countryOfIncorporation.error.length = CountryOfIncorporation must be 2 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CountryOfIncorporationPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "countryOfIncorporation.title = countryOfIncorporation" >> ../conf/messages.cy
echo "countryOfIncorporation.heading = countryOfIncorporation" >> ../conf/messages.cy
echo "countryOfIncorporation.checkYourAnswersLabel = countryOfIncorporation" >> ../conf/messages.cy
echo "countryOfIncorporation.label = CountryOfIncorporation" >> ../conf/messages.cy
echo "countryOfIncorporation.error.required = Enter countryOfIncorporation" >> ../conf/messages.cy
echo "countryOfIncorporation.error.length = CountryOfIncorporation must be 2 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryOfIncorporationUserAnswersEntry: Arbitrary[(CountryOfIncorporationPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CountryOfIncorporationPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryOfIncorporationPage: Arbitrary[CountryOfIncorporationPage.type] =";\
    print "    Arbitrary(CountryOfIncorporationPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CountryOfIncorporationPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def countryOfIncorporation: Option[SummaryListRow] = answer(CountryOfIncorporationPage, routes.CountryOfIncorporationController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CountryOfIncorporationPage.toString -> CountryOfIncorporationPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration CountryOfIncorporation completed"
