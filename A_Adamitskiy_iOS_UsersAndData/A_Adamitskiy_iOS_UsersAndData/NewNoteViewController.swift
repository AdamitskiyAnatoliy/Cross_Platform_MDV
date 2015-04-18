//
//  NewNoteViewController.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import Foundation
import UIKit
import Parse

class NewNoteViewController: UIViewController {
    
    @IBOutlet var doneButton: MKButton!
    @IBOutlet var titleField: MKTextField!
    @IBOutlet var contentField: MKTextField!
    @IBOutlet var hoursField: MKTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        doneButton.cornerRadius = 30.0
        doneButton.layer.shadowOpacity = 0.50
        doneButton.layer.shadowRadius = 2.5
        doneButton.layer.shadowColor = UIColor.blackColor().CGColor
        doneButton.layer.shadowOffset = CGSize(width: 1.0, height: 5.0)
        
        titleField.floatingPlaceholderEnabled = true
        titleField.cornerRadius = 0
        titleField.placeholder = "Title"
        titleField.layer.borderColor = UIColor.clearColor().CGColor
        titleField.rippleLayerColor = UIColor.MKColor.LightBlue
        titleField.tintColor = UIColor.MKColor.LightBlue
        titleField.bottomBorderEnabled = true
        
        contentField.floatingPlaceholderEnabled = true
        contentField.cornerRadius = 0
        contentField.placeholder = "Note"
        contentField.layer.borderColor = UIColor.clearColor().CGColor
        contentField.rippleLayerColor = UIColor.MKColor.LightBlue
        contentField.tintColor = UIColor.MKColor.LightBlue
        contentField.bottomBorderEnabled = true
        
        hoursField.floatingPlaceholderEnabled = true
        hoursField.cornerRadius = 0
        hoursField.placeholder = "Hours"
        hoursField.layer.borderColor = UIColor.clearColor().CGColor
        hoursField.rippleLayerColor = UIColor.MKColor.LightBlue
        hoursField.tintColor = UIColor.MKColor.LightBlue
        hoursField.bottomBorderEnabled = true
    }
    
    @IBAction func done(sender: MKButton) {
        
        let user = PFUser.currentUser()
        var privateNote = PFObject(className:"Note")
        privateNote["title"] = "\(titleField.text)"
        privateNote["content"] = "\(contentField.text)"
        privateNote["hours"] = "\(hoursField.text)"
        privateNote.ACL = PFACL(user: user!)
        privateNote.saveInBackground()
        
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    @IBAction func back(sender: UIButton) {
        
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    override func touchesBegan(touches: Set<NSObject>, withEvent event: UIEvent) {
        self.view.endEditing(true);
    }
}
