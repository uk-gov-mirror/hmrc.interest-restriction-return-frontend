#!/bin/bash

echo ""
echo "Applying migration LimitedLiabilityPartnership"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "LimitedLiabilityPartnership" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ultimateParentCompany.LimitedLiabilityPartnershipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.ultimateParentCompany.LimitedLiabilityPartnershipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.ultimateParentCompany.LimitedLiabilityPartnershipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.ultimateParentCompany.LimitedLiabilityPartnershipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# LimitedLiabilityPartnershipPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "LimitedLiabilityPartnership.title = Is Company a Limited Liability Partnership" >> ../conf/messages.en
echo "LimitedLiabilityPartnership.heading = Is Company a Limited Liability Partnership" >> ../conf/messages.en
echo "LimitedLiabilityPartnership.checkYourAnswersLabel = Is Company a Limited Liability Partnership" >> ../conf/messages.en
echo "LimitedLiabilityPartnership.error.required = Select yes if Is Company a Limited Liability Partnership" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# LimitedLiabilityPartnershipPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "LimitedLiabilityPartnership.title = Is Company a Limited Liability Partnership" >> ../conf/messages.cy
echo "LimitedLiabilityPartnership.heading = Is Company a Limited Liability Partnership" >> ../conf/messages.cy
echo "LimitedLiabilityPartnership.checkYourAnswersLabel = Is Company a Limited Liability Partnership" >> ../conf/messages.cy
echo "LimitedLiabilityPartnership.error.required = Select yes if Is Company a Limited Liability Partnership" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLimitedLiabilityPartnershipUserAnswersEntry: Arbitrary[(LimitedLiabilityPartnershipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LimitedLiabilityPartnershipPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLimitedLiabilityPartnershipPage: Arbitrary[LimitedLiabilityPartnershipPage.type] =";\
    print "    Arbitrary(LimitedLiabilityPartnershipPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LimitedLiabilityPartnershipPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def limitedLiabilityPartnership: Option[SummaryListRow] = answer(LimitedLiabilityPartnershipPage, routes.LimitedLiabilityPartnershipController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    LimitedLiabilityPartnershipPage.toString -> LimitedLiabilityPartnershipPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration LimitedLiabilityPartnership completed"
