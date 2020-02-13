This project has been created using MVVM architecture.



There are 2 activities:
1)SplashScreenActivity
2)MainActivity

To avoid the multiple network call in every configuration changes(Screen rotation/ App going in background) I am using Single source of truth Principle.
We are making the network call only once i.e on the SplashScreen and storign that data in our Room DB. Moving from splash screen we are only consuming what is there in the local DB.
For image proocessing we are using the Picasso library.

We have 3 url to simulate different situation.
We can pass different url to Connectivity Manager to simulated different situation
1)UrlUtils.EMPLOYEE_LIST_SUCCESS_URL
"https://s3.amazonaws.com/sq-mobile-interview/employees.json"
This URL will return a list of employee with out any malformed data.

2)UrlUtils.EMPLOYEE_LIST_MALFORMED_URL
"https://s3.amazonaws.com/sq-mobile-interview/employees_malformed.json";
This URL will return the list of employee with some malformed data.
In this case we scrap the whole list and return the empty list.

3)UrlUtils.EMPLOYEE_LIST_EMPTY_URL
This URL returns the empty list of employee.
"https://s3.amazonaws.com/sq-mobile-interview/employees_empty.json"


To replicate the Error case just Go to SplashScreenViewModel > getEmployeeListFromNetwork() > Replace the URL from the above list to see the Error cases.

For Example:
To see the Emply list response change this

connectivityManager.fetchFeedFromNetwork(UrlUtils.EMPLOYEE_LIST_SUCCESS_URL, Employees.class);

To

connectivityManager.fetchFeedFromNetwork(UrlUtils.EMPLOYEE_LIST_EMPTY_URL, Employees.class);
