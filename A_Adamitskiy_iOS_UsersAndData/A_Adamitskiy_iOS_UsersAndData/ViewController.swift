//
//  ViewController.swift
//  A_Adamitskiy_iOS_UsersAndData
//
//  Created by Anatoliy Adamitskiy on 4/13/15.
//  Copyright (c) 2015 Anatoliy Adamitskiy. All rights reserved.
//

import UIKit
import Parse

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet var addButton: MKButton!
    @IBOutlet var noteTableView: UITableView!
    var navBar:UINavigationBar=UINavigationBar()
    var notesArray = [AnyObject]()
    var pfObjectArray = [PFObject]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationController?.setNeedsStatusBarAppearanceUpdate()
        
        addButton.cornerRadius = 30.0
        addButton.layer.shadowOpacity = 0.50
        addButton.layer.shadowRadius = 2.5
        addButton.layer.shadowColor = UIColor.blackColor().CGColor
        addButton.layer.shadowOffset = CGSize(width: 1.0, height: 5.0)
        
        var timer = NSTimer.scheduledTimerWithTimeInterval(10.0, target: self,
            selector: Selector("refreshList"), userInfo: nil, repeats: true)
    }
    
    override func viewDidAppear(animated: Bool) {
        
        refreshList()
    }
    
    func refreshList () {
        
        if Reachability.isConnectedToNetwork() {
            
            var currentUser = PFUser.currentUser()
            if currentUser != nil {
                
                var pfQuery = PFQuery(className: "Note")
                pfQuery.findObjectsInBackgroundWithBlock({ (objects: Array?, error: NSError?) -> Void in
                    if (error == nil) {
                        
                        if let array = objects {
                            
                            self.pfObjectArray.removeAll(keepCapacity: false)
                            for var i = 0; i < array.count; ++i {
                                
                                self.pfObjectArray.append(array[i] as! PFObject)
                            }
                            self.noteTableView.reloadData()
                        }
                        
                    } else {
                        println("FAILURE")
                    }
                })
                
            } else {
                
                let logInVC: UIViewController = self.storyboard!.instantiateViewControllerWithIdentifier("LogInController") as! UIViewController
                self.presentViewController(logInVC, animated: true, completion: nil)
            }
        } else {
            let alert: UIAlertView = UIAlertView(title: "No Network Connection",
                message: "Please Reconnect Network.", delegate: nil, cancelButtonTitle: "Ok")
            alert.show()
        }
        println("LIST REFRESHED")
    }
    
    @IBAction func addNewNote(sender: MKButton) {
        if Reachability.isConnectedToNetwork() {
            self.navigationController!.pushViewController(self.storyboard!.instantiateViewControllerWithIdentifier("NewNoteController") as! UIViewController, animated: true)
        } else {
            let alert: UIAlertView = UIAlertView(title: "No Network Connection",
                message: "Please Reconnect Network.", delegate: nil, cancelButtonTitle: "Ok")
            alert.show()
        }
    }
    
    @IBAction func logOut(sender: UIButton) {
        
        PFUser.logOut()
        var currentUser = PFUser.currentUser()
        pfObjectArray.removeAll(keepCapacity: true)
        noteTableView.reloadData()
        
        let logInVC: UIViewController = self.storyboard!.instantiateViewControllerWithIdentifier("LogInController") as! UIViewController
        self.presentViewController(logInVC, animated: true, completion: nil)
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return pfObjectArray.count;
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        var cell: MyCell = tableView.dequeueReusableCellWithIdentifier("cell") as! MyCell
        cell.setMessage(pfObjectArray[indexPath.row]["title"] as! String)
        cell.rippleLayerColor = UIColor.MKColor.Blue
        
        return cell;
    }
    
    func tableView(tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        if section == tableView.numberOfSections() - 1 {
            
            var view: UIView = UIView();
            view.frame = CGRectZero;
            return view;
        }
        return nil
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
        if segue.identifier == "OpenDetailController" {
            
            let row = self.noteTableView.indexPathForSelectedRow()?.row
            let dvc = segue.destinationViewController as! DetailViewController
            dvc.noteTitle = pfObjectArray[row!]["title"] as! String
            dvc.noteContent = pfObjectArray[row!]["content"] as! String
            dvc.noteHours = pfObjectArray[row!]["hours"] as! String
            dvc.mainObject = pfObjectArray[row!]
        }
        
    }

}

