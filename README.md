# NewsApp
This is a reactive RESTful news application having an MVVP (model-view-View Model) type architecture. 

Tech. stack used: Java, Android SDK, ReactiveX android, Retrofit2, Room (persistent storage) and firebase job-dispatcher.

Functional requirements:
1.  A news feed showing the latest news based on the user's country.
2. Search for news articles in the feed section.
3. Store some news articles offline.
4. Everyday shows a notification with a piece of trending news to the user.
5. Users can filter news based on the news category.

Non-Functional requirements:
1. News articles are asynchronously loaded in the background to deliver faster results.
2. Articles are cached locally to provide a seamless user experience. 
3. MVVM type architecture is used, where there is a proper separation between the UI and the model components. This makes the code easy to maintain.
4. With MVVM, each piece of code is more granular, making unit testing easier.
