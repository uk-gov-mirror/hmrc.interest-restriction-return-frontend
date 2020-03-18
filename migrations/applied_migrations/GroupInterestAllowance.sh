#!/bin/bash

echo ""
echo "Applying migration GroupInterestAllowance"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /groupInterestAllowance                  controllers.groupLevelInformation.GroupInterestAllowanceController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /groupInterestAllowance                  controllers.groupLevelInformation.GroupInterestAllowanceController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGroupInterestAllowance                        controllers.groupLevelInformation.GroupInterestAllowanceController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGroupInterestAllowance                        controllers.groupLevelInformation.GroupInterestAllowanceController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# GroupInterestAllowancePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupInterestAllowance.title = GroupInterestAllowance" >> ../conf/messages.en
echo "groupInterestAllowance.heading = GroupInterestAllowance" >> ../conf/messages.en
echo "groupInterestAllowance.label = What is the group interest allowance for the period?" >> ../conf/messages.en
echo "groupInterestAllowance.checkYourAnswersLabel = GroupInterestAllowance" >> ../conf/messages.en
echo "groupInterestAllowance.error.nonNumeric = Enter your groupInterestAllowance using numbers" >> ../conf/messages.en
echo "groupInterestAllowance.error.required = Enter your groupInterestAllowance" >> ../conf/messages.en
echo "groupInterestAllowance.error.invalidNumeric = The groupInterestAllowance must be valid decimal or whole number" >> ../conf/messages.en
echo "groupInterestAllowance.error.outOfRange = GroupInterestAllowance must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# GroupInterestAllowancePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupInterestAllowance.title = GroupInterestAllowance" >> ../conf/messages.cy
echo "groupInterestAllowance.heading = GroupInterestAllowance" >> ../conf/messages.cy
echo "groupInterestAllowance.label =What is the group interest allowance for the period?" >> ../conf/messages.cy
echo "groupInterestAllowance.checkYourAnswersLabel = GroupInterestAllowance" >> ../conf/messages.cy
echo "groupInterestAllowance.error.nonNumeric = Enter your groupInterestAllowance using numbers" >> ../conf/messages.cy
echo "groupInterestAllowance.error.required = Enter your groupInterestAllowance" >> ../conf/messages.cy
echo "groupInterestAllowance.error.invalidNumeric = The groupInterestAllowance must be valid decimal or whole number" >> ../conf/messages.cy
echo "groupInterestAllowance.error.outOfRange = GroupInterestAllowance must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupInterestAllowanceUserAnswersEntry: Arbitrary[(GroupInterestAllowancePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupInterestAllowancePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupInterestAllowancePage: Arbitrary[GroupInterestAllowancePage.type] =";\
    print "    Arbitrary(GroupInterestAllowancePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupInterestAllowancePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupInterestAllowance: Option[SummaryListRow] = answer(GroupInterestAllowancePage, routes.GroupInterestAllowanceController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration GroupInterestAllowance completed"
