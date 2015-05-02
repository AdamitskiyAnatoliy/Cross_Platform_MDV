//
//  DetailViewController.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import Foundation
import UIKit
import Parse

class DetailViewController: UIViewController {
    
    var noteTitle = ""
    var noteContent = ""
    var noteHours = ""
    var mainObject:PFObject!
    @IBOutlet var deleteButton: MKButton!
    @IBOutlet var updateButton: MKButton!
    @IBOutlet var titleField: MKTextField!
    @IBOutlet var contentField: MKTextField!
    @IBOutlet var hoursField: MKTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        deleteButton.cornerRadius = 30.0
        deleteButton.layer.shadowOpacity = 0.50
        deleteButton.layer.shadowRadius = 2.5
        deleteButton.layer.shadowColor = UIColor.blackColor().CGColor
        deleteButton.layer.shadowOffset = CGSize(width: 1.0, height: 5.0)
        
        updateButton.cornerRadius = 30.0
        updateButton.layer.shadowOpacity = 0.50
        updateButton.layer.shadowRadius = 2.5
        updateButton.layer.shadowColor = UIColor.blackColor().CGColor
        updateButton.layer.shadowOffset = CGSize(width: 1.0, height: 5.0)
        
        titleField.floatingPlaceholderEnabled = true
        titleField.cornerRadius = 0
        titleField.placeholder = "Title"
        titleField.layer.borderColor = UIColor.clearColor().CGColor
        titleField.rippleLayerColor = UIColor.MKColor.LightBlue
        titleField.tintColor = UIColor.MKColor.LightBlue
        titleField.bottomBorderEnabled = true
        titleField.text = noteTitle
        
        contentField.floatingPlaceholderEnabled = true
        contentField.cornerRadius = 0
        contentField.placeholder = "Note"
        contentField.layer.borderColor = UIColor.clearColor().CGColor
        contentField.rippleLayerColor = UIColor.MKColor.LightBlue
        contentField.tintColor = UIColor.MKColor.LightBlue
        contentField.bottomBorderEnabled = true
        contentField.text = noteContent
        
        hoursField.floatingPlaceholderEnabled = true
        hoursField.cornerRadius = 0
        hoursField.placeholder = "Hours"
        hoursField.layer.borderColor = UIColor.clearColor().CGColor
        hoursField.rippleLayerColor = UIColor.MKColor.LightBlue
        hoursField.tintColor = UIColor.MKColor.LightBlue
        hoursField.bottomBorderEnabled = true
        hoursField.text = noteHours
        
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent
    }
    
    @IBAction func back(sender: UIButton) {
        
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    @IBAction func deleteNote(sender: MKButton) {
        
        if Reachability.isConnectedToNetwork() {
            mainObject.deleteInBackground()
            self.navigationController?.popViewControllerAnimated(true)
        } else {
            let alert: UIAlertView = UIAlertView(title: "No Network Connection",
                message: "Please Reconnect Network.", delegate: nil, cancelButtonTitle: "Ok")
            alert.show()
        }
    }
    
    @IBAction func updateNote(sender: MKButton) {
        
        if Reachability.isConnectedToNetwork() {
            
            if titleField.text == "" || contentField.text == "" || hoursField.text == "" {
                
                let alert: UIAlertView = UIAlertView(title: "Fill Out all Fields",
                    message: "Please Fill Out Entire Form.", delegate: nil, cancelButtonTitle: "Ok")
                alert.show()
            } else {
                
                var query = PFQuery(className:"Note")
                query.getObjectInBackgroundWithId(mainObject.objectId!) {
                    (privateNote: PFObject?, error: NSError?) -> Void in
                    if error != nil {
                        println(error)
                    } else if let privateNote = privateNote {
                        privateNote["title"] = "\(self.titleField.text)"
                        privateNote["content"] = "\(self.contentField.text)"
                        privateNote["hours"] = "\(self.hoursField.text)"
                        privateNote["noteType"] = "text"
                        privateNote.saveInBackground()
                    }
                }
                self.navigationController?.popViewControllerAnimated(true)
            }
        } else {
            let alert: UIAlertView = UIAlertView(title: "No Network Connection",
                message: "Please Reconnect Network.", delegate: nil, cancelButtonTitle: "Ok")
            alert.show()
        }
    }
    
    override func touchesBegan(touches: Set<NSObject>, withEvent event: UIEvent) {
        self.view.endEditing(true);
    }
}
