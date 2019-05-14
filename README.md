# GithubRepos

#### This sample was created to showcase my skills in the Android Framework.

In the company I work at, we use MVP architecture, dagger2, GSON, and no use of livedata and other components. 
I decided in this project to make use of the jetpack libraries, especially that almost all of them are now stable.

I used the **MVVM** architecture with a flavor of state machine approach inorder to render the UI according to the 
viewmodel's state. 

I picked Koin as the DI framework, to try it out my self. After reading lots of debates about Koin and Dagger2.

Second, I used the ViewModel component, which is very nifty library IMHO

Also, I opted out to use the paging library from jetpack to experiment with it. Its approach is really neat. 
The result is amazing (check below the gif) 

I decided to go with Moshi this time instead of GSON for serializing/deserializing kotlin models into and from json, as its – as the core maintainers say – GSON 3.0 but without backward support. It is created by the same people who created GSON. I was pretty satisfied with it, especially with the newly added codegen feature.



Here is a screenshot of the only screen in the app


![Main screen screenshot](/screenshots/github_repos_image.png)


![App Gif demo](/screenshots/github_repo_gif.gif)
