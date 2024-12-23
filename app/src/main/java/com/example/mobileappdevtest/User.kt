package com.example.mobileappdevtest

class User {
    var userid: String = ""
    var username: String = ""
    var usermail: String = ""
    var userpassword: String = ""
    var usergender: String = ""
    var userimageuri: String = ""

    // Default constructor
    constructor()

    // Parameterized constructor
    constructor(
        userid: String,
        username: String,
        usermail: String,
        userpassword: String,
        usergender: String,
        userimageuri: String
    ) {
        this.userid = userid
        this.username = username
        this.usermail = usermail
        this.userpassword = userpassword
        this.usergender = usergender
        this.userimageuri = userimageuri
    }
}