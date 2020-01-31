#!/bin/bash

echo ""
echo "Applying migration GroupInterestCapacity"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /groupInterestCapacity                  controllers.aboutReturn.GroupInterestCapacityController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /groupInterestCapacity                  controllers.aboutReturn.GroupInterestCapacityController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGroupInterestCapacity                        controllers.aboutReturn.GroupInterestCapacityController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGroupInterestCapacity                        controllers.aboutReturn.GroupInterestCapacityController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# GroupInterestCapacityPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupInterestCapacity.title = GroupInterestCapacity" >> ../conf/messages.en
echo "groupInterestCapacity.heading = GroupInterestCapacity" >> ../conf/messages.en
echo "groupInterestCapacity.label = What is the group interest capacity for the period?" >> ../conf/messages.en
echo "groupInterestCapacity.checkYourAnswersLabel = GroupInterestCapacity" >> ../conf/messages.en
echo "groupInterestCapacity.error.nonNumeric = Enter your groupInterestCapacity using numbers" >> ../conf/messages.en
echo "groupInterestCapacity.error.required = Enter your groupInterestCapacity" >> ../conf/messages.en
echo "groupInterestCapacity.error.invalidNumeric = The groupInterestCapacity must be valid decimal or whole number" >> ../conf/messages.en
echo "groupInterestCapacity.error.outOfRange = GroupInterestCapacity must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# GroupInterestCapacityPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupInterestCapacity.title = GroupInterestCapacity" >> ../conf/messages.cy
echo "groupInterestCapacity.heading = GroupInterestCapacity" >> ../conf/messages.cy
echo "groupInterestCapacity.label =What is the group interest capacity for the period?" >> ../conf/messages.cy
echo "groupInterestCapacity.checkYourAnswersLabel = GroupInterestCapacity" >> ../conf/messages.cy
echo "groupInterestCapacity.error.nonNumeric = Enter your groupInterestCapacity using numbers" >> ../conf/messages.cy
echo "groupInterestCapacity.error.required = Enter your groupInterestCapacity" >> ../conf/messages.cy
echo "groupInterestCapacity.error.invalidNumeric = The groupInterestCapacity must be valid decimal or whole number" >> ../conf/messages.cy
echo "groupInterestCapacity.error.outOfRange = GroupInterestCapacity must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupInterestCapacityUserAnswersEntry: Arbitrary[(GroupInterestCapacityPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupInterestCapacityPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupInterestCapacityPage: Arbitrary[GroupInterestCapacityPage.type] =";\
    print "    Arbitrary(GroupInterestCapacityPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupInterestCapacityPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupInterestCapacity: Option[SummaryListRow] = answer(GroupInterestCapacityPage, routes.GroupInterestCapacityController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala


echo "Migration GroupInterestCapacity completed"
