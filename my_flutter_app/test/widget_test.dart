import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:my_flutter_app/screens/home_screen.dart';

void main() {
  testWidgets('HomeScreen has a title and a message', (WidgetTester tester) async {
    await tester.pumpWidget(MaterialApp(home: HomeScreen()));

    final titleFinder = find.text('Home Screen Title'); // Replace with actual title
    final messageFinder = find.text('Welcome to the Home Screen!'); // Replace with actual message

    expect(titleFinder, findsOneWidget);
    expect(messageFinder, findsOneWidget);
  });
}