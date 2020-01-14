#!/bin/bash

echo ""
echo "Applying migration InterestReactivationsCap"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /interestReactivationsCap                  controllers.InterestReactivationsCapController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /interestReactivationsCap                  controllers.InterestReactivationsCapController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeInterestReactivationsCap                        controllers.InterestReactivationsCapController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeInterestReactivationsCap                        controllers.InterestReactivationsCapController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# InterestReactivationsCapPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "interestReactivationsCap.title = InterestReactivationsCap" >> ../conf/messages.en
echo "interestReactivationsCap.heading = InterestReactivationsCap" >> ../conf/messages.en
echo "interestReactivationsCap.label = interestReactivationsCap" >> ../conf/messages.en
echo "interestReactivationsCap.checkYourAnswersLabel = InterestReactivationsCap" >> ../conf/messages.en
echo "interestReactivationsCap.error.nonNumeric = Enter your interestReactivationsCap using numbers" >> ../conf/messages.en
echo "interestReactivationsCap.error.required = Enter your interestReactivationsCap" >> ../conf/messages.en
echo "interestReactivationsCap.error.invalidNumeric = The interestReactivationsCap must be valid decimal or whole number" >> ../conf/messages.en
echo "interestReactivationsCap.error.outOfRange = InterestReactivationsCap must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# InterestReactivationsCapPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "interestReactivationsCap.title = InterestReactivationsCap" >> ../conf/messages.cy
echo "interestReactivationsCap.heading = InterestReactivationsCap" >> ../conf/messages.cy
echo "interestReactivationsCap.label =interestReactivationsCap" >> ../conf/messages.cy
echo "interestReactivationsCap.checkYourAnswersLabel = InterestReactivationsCap" >> ../conf/messages.cy
echo "interestReactivationsCap.error.nonNumeric = Enter your interestReactivationsCap using numbers" >> ../conf/messages.cy
echo "interestReactivationsCap.error.required = Enter your interestReactivationsCap" >> ../conf/messages.cy
echo "interestReactivationsCap.error.invalidNumeric = The interestReactivationsCap must be valid decimal or whole number" >> ../conf/messages.cy
echo "interestReactivationsCap.error.outOfRange = InterestReactivationsCap must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestReactivationsCapUserAnswersEntry: Arbitrary[(InterestReactivationsCapPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InterestReactivationsCapPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInterestReactivationsCapPage: Arbitrary[InterestReactivationsCapPage.type] =";\
    print "    Arbitrary(InterestReactivationsCapPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InterestReactivationsCapPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interestReactivationsCap: Option[SummaryListRow] = answer(InterestReactivationsCapPage, routes.InterestReactivationsCapController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object InterestReactivationsCapTemplate extends WithName(\"interestReactivationsCap.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration InterestReactivationsCap completed"
