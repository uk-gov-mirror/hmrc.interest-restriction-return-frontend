#!/bin/bash

echo ""
echo "Applying migration ReturnContainEstimates"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /returnContainEstimates                        controllers.aboutReturn.ReturnContainEstimatesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /returnContainEstimates                        controllers.aboutReturn.ReturnContainEstimatesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeReturnContainEstimates                  controllers.aboutReturn.ReturnContainEstimatesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeReturnContainEstimates                  controllers.aboutReturn.ReturnContainEstimatesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ReturnContainEstimatesPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "returnContainEstimates.title = Does the return contain estimates?" >> ../conf/messages.en
echo "returnContainEstimates.heading = Does the return contain estimates?" >> ../conf/messages.en
echo "returnContainEstimates.checkYourAnswersLabel = Does the return contain estimates?" >> ../conf/messages.en
echo "returnContainEstimates.error.required = Select yes if Does the return contain estimates?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ReturnContainEstimatesPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "returnContainEstimates.title = Does the return contain estimates?" >> ../conf/messages.cy
echo "returnContainEstimates.heading = Does the return contain estimates?" >> ../conf/messages.cy
echo "returnContainEstimates.checkYourAnswersLabel = Does the return contain estimates?" >> ../conf/messages.cy
echo "returnContainEstimates.error.required = Select yes if Does the return contain estimates?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReturnContainEstimatesUserAnswersEntry: Arbitrary[(ReturnContainEstimatesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ReturnContainEstimatesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryReturnContainEstimatesPage: Arbitrary[ReturnContainEstimatesPage.type] =";\
    print "    Arbitrary(ReturnContainEstimatesPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ReturnContainEstimatesPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def returnContainEstimates: Option[SummaryListRow] = answer(ReturnContainEstimatesPage, routes.ReturnContainEstimatesController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object ReturnContainEstimatesTemplate extends WithName(\"returnContainEstimates.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration ReturnContainEstimates completed"
