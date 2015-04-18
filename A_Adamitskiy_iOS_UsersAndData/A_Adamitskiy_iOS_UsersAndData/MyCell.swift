//
//  MyCell.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import UIKit

class MyCell : MKTableViewCell {
    @IBOutlet var messageLabel: UILabel!
   
    override var layoutMargins: UIEdgeInsets {
        get { return UIEdgeInsetsZero }
        set(newVal) {}
    }
    
    func setMessage(message: String) {
        messageLabel.text = message
    }
}
