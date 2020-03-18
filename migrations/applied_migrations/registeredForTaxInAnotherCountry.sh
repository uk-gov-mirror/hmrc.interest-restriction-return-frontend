#!/bin/bash

echo ""
echo "Applying migration registeredForTaxInAnotherCountry"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "registeredForTaxInAnotherCountry" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ultimateParentCompany.RegisteredForTaxInAnotherCountryController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.ultimateParentCompany.RegisteredForTaxInAnotherCountryController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.ultimateParentCompany.RegisteredForTaxInAnotherCountryController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.ultimateParentCompany.RegisteredForTaxInAnotherCountryController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# registeredForTaxInAnotherCountryPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "registeredForTaxInAnotherCountry.title = registeredForTaxInAnotherCountry" >> ../conf/messages.en
echo "registeredForTaxInAnotherCountry.heading = registeredForTaxInAnotherCountry" >> ../conf/messages.en
echo "registeredForTaxInAnotherCountry.checkYourAnswersLabel = registeredForTaxInAnotherCountry" >> ../conf/messages.en
echo "registeredForTaxInAnotherCountry.error.required = Select yes if registeredForTaxInAnotherCountry" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# registeredForTaxInAnotherCountryPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "registeredForTaxInAnotherCountry.title = registeredForTaxInAnotherCountry" >> ../conf/messages.cy
echo "registeredForTaxInAnotherCountry.heading = registeredForTaxInAnotherCountry" >> ../conf/messages.cy
echo "registeredForTaxInAnotherCountry.checkYourAnswersLabel = registeredForTaxInAnotherCountry" >> ../conf/messages.cy
echo "registeredForTaxInAnotherCountry.error.required = Select yes if registeredForTaxInAnotherCountry" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegisteredForTaxInAnotherCountryUserAnswersEntry: Arbitrary[(RegisteredForTaxInAnotherCountryPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RegisteredForTaxInAnotherCountryPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRegisteredForTaxInAnotherCountryPage: Arbitrary[RegisteredForTaxInAnotherCountryPage.type] =";\
    print "    Arbitrary(RegisteredForTaxInAnotherCountryPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RegisteredForTaxInAnotherCountryPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def registeredForTaxInAnotherCountry: Option[SummaryListRow] = answer(RegisteredForTaxInAnotherCountryPage, routes.RegisteredForTaxInAnotherCountryController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    RegisteredForTaxInAnotherCountryPage.toString -> RegisteredForTaxInAnotherCountryPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration registeredForTaxInAnotherCountry completed"
