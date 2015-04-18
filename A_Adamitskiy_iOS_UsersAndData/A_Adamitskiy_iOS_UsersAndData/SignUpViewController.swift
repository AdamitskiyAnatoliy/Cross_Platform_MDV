//
//  SignUpViewController.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import Foundation
import UIKit
import Parse

class SignUpViewController: UIViewController {
    
    @IBOutlet var usernameField: MKTextField!
    @IBOutlet var passwordField: MKTextField!
    @IBOutlet var passConfirmField: MKTextField!
    
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
        
        passConfirmField.layer.borderColor = UIColor.clearColor().CGColor
        passConfirmField.placeholder = "Confirm Password"
        passConfirmField.rippleLayerColor = UIColor.MKColor.LightBlue
        passConfirmField.tintColor = UIColor.MKColor.Blue
        passConfirmField.backgroundColor = UIColor(hex: 0xffffff)
    }
    
    @IBAction func signUp(sender: MKButton) {
        
        var user = PFUser()
        user.username = "\(usernameField.text)"
        
        if (passwordField.text == passConfirmField.text) {
            
            user.password = "\(passwordField.text)"
            user.signUpInBackgroundWithBlock({ (succeeded: Bool, error: NSError?) -> Void in
                if error == nil {
                    
                    self.navigationController?.popViewControllerAnimated(true);
                    
                } else {
                    
                    let alert: UIAlertView = UIAlertView(title: "There was an Error.",
                        message: "Please try again.", delegate: nil, cancelButtonTitle: "Ok")
                    alert.show()
                    
                }
            })
        } else {
            
            let alert: UIAlertView = UIAlertView(title: "Passwords do not match.",
                message: "Please try again.", delegate: nil, cancelButtonTitle: "Ok")
            alert.show()
        }
    }
}
