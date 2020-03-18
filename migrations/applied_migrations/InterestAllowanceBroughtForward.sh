#!/bin/bash

echo ""
echo "Applying migration InterestAllowanceBroughtForward"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /interestAllowanceBroughtForward                  controllers.groupLevelInformation.InterestAllowanceBroughtForwardController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /interestAllowanceBroughtForward                  controllers.groupLevelInformation.InterestAllowanceBroughtForwardController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeInterestAllowanceBroughtForward                        controllers.groupLevelInformation.InterestAllowanceBroughtForwardController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeInterestAllowanceBroughtForward                        controllers.groupLevelInformation.InterestAllowanceBroughtForwardController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# InterestAllowanceBroughtForwardPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.title = InterestAllowanceBroughtForward" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.heading = InterestAllowanceBroughtForward" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.label = What is the group interest allowance brought forward?" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.checkYourAnswersLabel = InterestAllowanceBroughtForward" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.error.nonNumeric = Enter your interestAllowanceBroughtForward using numbers" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.error.required = Enter your interestAllowanceBroughtForward" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.error.wholeNumber = Enter your interestAllowanceBroughtForward using whole numbers" >> ../conf/messages.en
echo "interestAllowanceBroughtForward.error.outOfRange = InterestAllowanceBroughtForward must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# InterestAllowanceBroughtForwardPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.title = InterestAllowanceBroughtForward" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.heading = InterestAllowanceBroughtForward" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.label =What is the group interest allowance brought forward?" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.checkYourAnswersLabel = InterestAllowanceBroughtForward" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.error.nonNumeric = Enter your interestAllowanceBroughtForward using numbers" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.error.required = Enter your interestAllowanceBroughtForward" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.error.wholeNumber = Enter your interestAllowanceBroughtForward using whole numbers" >> ../conf/messages.cy
echo "interestAllowanceBroughtForward.error.outOfRange = InterestAllowanceBroughtForward must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceBroughtForwardUserAnswersEntry: Arbitrary[(InterestAllowanceBroughtForwardPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InterestAllowanceBroughtForwardPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestAllowanceBroughtForwardPage: Arbitrary[InterestAllowanceBroughtForwardPage.type] =";\
    print "    Arbitrary(InterestAllowanceBroughtForwardPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InterestAllowanceBroughtForwardPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interestAllowanceBroughtForward: Option[SummaryListRow] = answer(InterestAllowanceBroughtForwardPage, routes.InterestAllowanceBroughtForwardController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration InterestAllowanceBroughtForward completed"
