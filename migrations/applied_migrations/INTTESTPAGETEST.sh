#!/bin/bash

echo ""
echo "Applying migration INTTESTPAGETEST"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /iNTTESTPAGETEST                  controllers.INTTESTPAGETESTController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /iNTTESTPAGETEST                  controllers.INTTESTPAGETESTController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeINTTESTPAGETEST                        controllers.INTTESTPAGETESTController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeINTTESTPAGETEST                        controllers.INTTESTPAGETESTController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# INTTESTPAGETESTPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "iNTTESTPAGETEST.title = INTTESTPAGETEST" >> ../conf/messages.en
echo "iNTTESTPAGETEST.heading = INTTESTPAGETEST" >> ../conf/messages.en
echo "iNTTESTPAGETEST.checkYourAnswersLabel = INTTESTPAGETEST" >> ../conf/messages.en
echo "iNTTESTPAGETEST.error.nonNumeric = Enter your iNTTESTPAGETEST using numbers" >> ../conf/messages.en
echo "iNTTESTPAGETEST.error.required = Enter your iNTTESTPAGETEST" >> ../conf/messages.en
echo "iNTTESTPAGETEST.error.wholeNumber = Enter your iNTTESTPAGETEST using whole numbers" >> ../conf/messages.en
echo "iNTTESTPAGETEST.error.outOfRange = INTTESTPAGETEST must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# INTTESTPAGETESTPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.title = INTTESTPAGETEST" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.heading = INTTESTPAGETEST" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.checkYourAnswersLabel = INTTESTPAGETEST" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.error.nonNumeric = Enter your iNTTESTPAGETEST using numbers" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.error.required = Enter your iNTTESTPAGETEST" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.error.wholeNumber = Enter your iNTTESTPAGETEST using whole numbers" >> ../conf/messages.cy
echo "iNTTESTPAGETEST.error.outOfRange = INTTESTPAGETEST must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryINTTESTPAGETESTUserAnswersEntry: Arbitrary[(INTTESTPAGETESTPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[INTTESTPAGETESTPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryINTTESTPAGETESTPage: Arbitrary[INTTESTPAGETESTPage.type] =";\
    print "    Arbitrary(INTTESTPAGETESTPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(INTTESTPAGETESTPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def iNTTESTPAGETEST: Option[SummaryListRow] = answer(INTTESTPAGETESTPage, routes.INTTESTPAGETESTController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration INTTESTPAGETEST completed"
