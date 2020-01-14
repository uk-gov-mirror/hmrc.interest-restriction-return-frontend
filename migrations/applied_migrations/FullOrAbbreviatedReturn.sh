#!/bin/bash

echo ""
echo "Applying migration FullOrAbbreviatedReturn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /fullOrAbbreviatedReturn                        controllers.startReturn.FullOrAbbreviatedReturnController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /fullOrAbbreviatedReturn                        controllers.startReturn.FullOrAbbreviatedReturnController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeFullOrAbbreviatedReturn                  controllers.startReturn.FullOrAbbreviatedReturnController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeFullOrAbbreviatedReturn                  controllers.startReturn.FullOrAbbreviatedReturnController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# FullOrAbbreviatedReturnPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.title = Do you want to submit a full or abbreviated return?" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.heading = Do you want to submit a full or abbreviated return?" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.full = Submit a full return" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.abbreviated = Submit an abbreviated return" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.checkYourAnswersLabel = Do you want to submit a full or abbreviated return?" >> ../conf/messages.en
echo "fullOrAbbreviatedReturn.error.required = Select fullOrAbbreviatedReturn" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# FullOrAbbreviatedReturnPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.title = Do you want to submit a full or abbreviated return?" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.heading = Do you want to submit a full or abbreviated return?" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.full = Submit a full return" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.abbreviated = Submit an abbreviated return" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.checkYourAnswersLabel = Do you want to submit a full or abbreviated return?" >> ../conf/messages.cy
echo "fullOrAbbreviatedReturn.error.required = Select fullOrAbbreviatedReturn" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryFullOrAbbreviatedReturnUserAnswersEntry: Arbitrary[(FullOrAbbreviatedReturnPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[FullOrAbbreviatedReturnPage.type]";\
    print "        value <- arbitrary[FullOrAbbreviatedReturn].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryFullOrAbbreviatedReturnPage: Arbitrary[FullOrAbbreviatedReturnPage.type] =";\
    print "    Arbitrary(FullOrAbbreviatedReturnPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryFullOrAbbreviatedReturn: Arbitrary[FullOrAbbreviatedReturn] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(FullOrAbbreviatedReturn.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(FullOrAbbreviatedReturnPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "  def fullOrAbbreviatedReturn: Option[SummaryListRow] = answer(FullOrAbbreviatedReturnPage, routes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object FullOrAbbreviatedReturnTemplate extends WithName(\"fullOrAbbreviatedReturn.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration FullOrAbbreviatedReturn completed"
