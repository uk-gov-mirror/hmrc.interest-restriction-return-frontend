#!/bin/bash

echo ""
echo "Applying migration ContinueSavedReturn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /continueSavedReturn                        controllers.ContinueSavedReturnController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /continueSavedReturn                        controllers.ContinueSavedReturnController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeContinueSavedReturn                  controllers.ContinueSavedReturnController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeContinueSavedReturn                  controllers.ContinueSavedReturnController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ContinueSavedReturnPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "continueSavedReturn.title = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.en
echo "continueSavedReturn.heading = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.en
echo "continueSavedReturn.newReturn = Start a new return" >> ../conf/messages.en
echo "continueSavedReturn.continueSavedReturn = Continue working on a saved return" >> ../conf/messages.en
echo "continueSavedReturn.checkYourAnswersLabel = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.en
echo "continueSavedReturn.error.required = Select continueSavedReturn" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ContinueSavedReturnPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "continueSavedReturn.title = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.cy
echo "continueSavedReturn.heading = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.cy
echo "continueSavedReturn.newReturn = Start a new return" >> ../conf/messages.cy
echo "continueSavedReturn.continueSavedReturn = Continue working on a saved return" >> ../conf/messages.cy
echo "continueSavedReturn.checkYourAnswersLabel = Do you want to start a new return or continue working on a saved return (if you have one)?" >> ../conf/messages.cy
echo "continueSavedReturn.error.required = Select continueSavedReturn" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContinueSavedReturnUserAnswersEntry: Arbitrary[(ContinueSavedReturnPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ContinueSavedReturnPage.type]";\
    print "        value <- arbitrary[ContinueSavedReturn].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContinueSavedReturnPage: Arbitrary[ContinueSavedReturnPage.type] =";\
    print "    Arbitrary(ContinueSavedReturnPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContinueSavedReturn: Arbitrary[ContinueSavedReturn] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ContinueSavedReturn.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ContinueSavedReturnPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "  def continueSavedReturn: Option[SummaryListRow] = answer(ContinueSavedReturnPage, routes.ContinueSavedReturnController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ContinueSavedReturn completed"
