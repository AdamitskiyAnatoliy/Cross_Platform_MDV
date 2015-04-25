//
//  LoginViewController.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import Foundation
import UIKit
import Parse

class LoginViewController: UIViewController {
    
    @IBOutlet var usernameField: MKTextField!
    @IBOutlet var passwordField: MKTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        usernameField.layer.borderColor = UIColor.clearColor().CGColor
        usernameField.placeholder = "Username"
        usernameField.rippleLayerColor = UIColor.MKColor.LightBlue
        usernameField.tintColor = UIColor.MKColor.Blue
        usernameField.backgroundColor = UIColor(hex: 0xffffff)
        
        passwordField.layer.borderColor = UIColor.clearColor().CGColor
        passwordField.placeholder = "Password"
        passwordField.rippleLayerColor = UIColor.MKColor.LightBlue
        passwordField.tintColor = UIColor.MKColor.Blue
        passwordField.backgroundColor = UIColor(hex: 0xffffff)
        
    }
    
    @IBAction func logIn(sender: MKButton) {
        
        PFUser.logInWithUsernameInBackground(usernameField.text, password: passwordField.text) {
            (user: PFUser?, error: NSError?) -> Void in
            if user != nil {

                self.dismissViewControllerAnimated(true, completion: nil)
            } else {
                
                let alert: UIAlertView = UIAlertView(title: "Login Failed.",
                    message: "Please try again.", delegate: nil, cancelButtonTitle: "Ok")
                alert.show()
            }
        }
    }
    
    @IBAction func signUp(sender: MKButton) {
        
        let logInVC: UIViewController = self.storyboard!.instantiateViewControllerWithIdentifier("SignUpController") as! UIViewController
        self.presentViewController(logInVC, animated: true, completion: nil)
        //self.navigationController!.pushViewController(self.storyboard!.instantiateViewControllerWithIdentifier("SignUpController") as! UIViewController, animated: true)
    }
    
    
}
