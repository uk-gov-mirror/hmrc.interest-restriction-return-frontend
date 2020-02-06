#!/bin/bash

echo ""
echo "Applying migration $className;format="snake"$"

echo "" >> ../conf/app.routes
echo "### $className;format="cap"$ Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes
echo "GET        /\$kebabClassName                         controllers.$className;format="cap"$Controller.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "$className;format="decap"$.title = $className;format="decap"$" >> ../conf/messages.en
echo "$className;format="decap"$.heading = $className;format="decap"$" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "$className;format="decap"$.title = $className;format="decap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.heading = $className;format="decap"$" >> ../conf/messages.cy

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    $className;format="cap"$Page.toString -> $className;format="cap"$Page,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    $className;format="cap"$Page.toString -> $className;format="cap"$Page,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val $className;format="decap"$ = \"$title$\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

echo "Migration $className;format="snake"$ completed"
