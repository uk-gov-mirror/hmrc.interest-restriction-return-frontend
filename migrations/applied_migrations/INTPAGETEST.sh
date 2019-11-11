#!/bin/bash

echo ""
echo "Applying migration INTPAGETEST"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /iNTPAGETEST                  controllers.INTPAGETESTController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /iNTPAGETEST                  controllers.INTPAGETESTController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeINTPAGETEST                        controllers.INTPAGETESTController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeINTPAGETEST                        controllers.INTPAGETESTController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# INTPAGETESTPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "iNTPAGETEST.title = INTPAGETEST" >> ../conf/messages.en
echo "iNTPAGETEST.heading = INTPAGETEST" >> ../conf/messages.en
echo "iNTPAGETEST.checkYourAnswersLabel = INTPAGETEST" >> ../conf/messages.en
echo "iNTPAGETEST.error.nonNumeric = Enter your iNTPAGETEST using numbers" >> ../conf/messages.en
echo "iNTPAGETEST.error.required = Enter your iNTPAGETEST" >> ../conf/messages.en
echo "iNTPAGETEST.error.wholeNumber = Enter your iNTPAGETEST using whole numbers" >> ../conf/messages.en
echo "iNTPAGETEST.error.outOfRange = INTPAGETEST must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# INTPAGETESTPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "iNTPAGETEST.title = INTPAGETEST" >> ../conf/messages.cy
echo "iNTPAGETEST.heading = INTPAGETEST" >> ../conf/messages.cy
echo "iNTPAGETEST.checkYourAnswersLabel = INTPAGETEST" >> ../conf/messages.cy
echo "iNTPAGETEST.error.nonNumeric = Enter your iNTPAGETEST using numbers" >> ../conf/messages.cy
echo "iNTPAGETEST.error.required = Enter your iNTPAGETEST" >> ../conf/messages.cy
echo "iNTPAGETEST.error.wholeNumber = Enter your iNTPAGETEST using whole numbers" >> ../conf/messages.cy
echo "iNTPAGETEST.error.outOfRange = INTPAGETEST must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryINTPAGETESTUserAnswersEntry: Arbitrary[(INTPAGETESTPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[INTPAGETESTPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryINTPAGETESTPage: Arbitrary[INTPAGETESTPage.type] =";\
    print "    Arbitrary(INTPAGETESTPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(INTPAGETESTPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def iNTPAGETEST: Option[SummaryListRow] = answer(INTPAGETESTPage, routes.INTPAGETESTController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration INTPAGETEST completed"
