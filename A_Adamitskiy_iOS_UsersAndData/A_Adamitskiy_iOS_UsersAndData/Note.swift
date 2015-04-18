//
//  Note.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/17/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import Foundation

class Note {
    
    var title = ""
    var content = ""
    var hours = ""
    
    init (_title: String, _content: String, _hours: String) {
        title = _title
        content = _content
        hours = _hours
    }
    
}