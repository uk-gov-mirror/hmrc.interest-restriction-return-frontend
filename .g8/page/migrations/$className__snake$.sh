#!/bin/bash

echo ""
echo "Applying migration $className;format="snake"$"

echo "" >> ../conf/app.routes
echo "GET        /$className;format="decap"$                            controllers.$className;format="cap"$Controller.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# $className$Page Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "$className$.title = $className;format="decap"$" >> ../conf/messages.en
echo "$className$.heading = $className;format="decap"$" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# $className$Page Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "$className$.title = $className;format="decap"$" >> ../conf/messages.cy
echo "$className$.heading = $className;format="decap"$" >> ../conf/messages.cy

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    $className;format="cap"$Page.toString -> $className;format="cap"$Page,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "Migration $className;format="snake"$ completed"

export kebabClassName=\$(sed --expression 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' --expression 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "$className$" | tr '[:upper:]' '[:lower:]')
echo "GET        /\$kebabClassName                          controllers.$className;format="cap"$Controller".onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes

