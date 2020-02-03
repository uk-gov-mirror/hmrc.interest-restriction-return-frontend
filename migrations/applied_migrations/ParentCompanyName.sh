#!/bin/bash

echo ""
echo "Applying migration ParentCompanyName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /parentCompanyName                        controllers.groupStructure.ParentCompanyNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /parentCompanyName                        controllers.groupStructure.ParentCompanyNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeParentCompanyName                  controllers.groupStructure.ParentCompanyNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeParentCompanyName                  controllers.groupStructure.ParentCompanyNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# ParentCompanyNamePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "parentCompanyName.title = parentCompanyName" >> ../conf/messages.en
echo "parentCompanyName.heading = parentCompanyName" >> ../conf/messages.en
echo "parentCompanyName.checkYourAnswersLabel = parentCompanyName" >> ../conf/messages.en
echo "parentCompanyName.label = MyNewPage" >> ../conf/messages.en
echo "parentCompanyName.error.required = Enter parentCompanyName" >> ../conf/messages.en
echo "parentCompanyName.error.length = ParentCompanyName must be 100 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# ParentCompanyNamePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "parentCompanyName.title = parentCompanyName" >> ../conf/messages.cy
echo "parentCompanyName.heading = parentCompanyName" >> ../conf/messages.cy
echo "parentCompanyName.checkYourAnswersLabel = parentCompanyName" >> ../conf/messages.cy
echo "parentCompanyName.label = MyNewPage" >> ../conf/messages.cy
echo "parentCompanyName.error.required = Enter parentCompanyName" >> ../conf/messages.cy
echo "parentCompanyName.error.length = ParentCompanyName must be 100 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanyNameUserAnswersEntry: Arbitrary[(ParentCompanyNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ParentCompanyNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryParentCompanyNamePage: Arbitrary[ParentCompanyNamePage.type] =";\
    print "    Arbitrary(ParentCompanyNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ParentCompanyNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def parentCompanyName: Option[SummaryListRow] = answer(ParentCompanyNamePage, routes.ParentCompanyNameController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ParentCompanyNamePage.toString -> ParentCompanyNamePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration ParentCompanyName completed"
