#!/usr/bin/perl
#Desription
#This script has been used to assign to each staff member a module to teach.
use strict;
use warnings;
use DBI;
use experimental 'smartmatch';

my @staff_ids;
my @module_ids;

# Connect to the database.
my $dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});

sub select_staff{
my $sth =$dbh->prepare("SELECT * FROM android_staff") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@staff_ids, $ref->{'staffid'});
}
$sth->finish();
}

sub select_modules{
my $sth =$dbh->prepare("SELECT * FROM android_module") ;
$sth->execute();
while (my $ref = $sth->fetchrow_hashref()) {
    #print "Found a row:id = $ref->{'matric_number'}, first_name = $ref->{'first_name'}, last_name = $ref->{'last_name'}\n";
    push(@module_ids, $ref->{'moduleid'});
}
$sth->finish();
}


sub addmodule_staff{
    my $total_modules=scalar(@module_ids);
    my $total_staff= scalar(@staff_ids);

    my @holder;
    if( $total_staff==$total_modules){
        print $total_staff, " ", $total_modules,"\n";

    for(my $i=0; $i<$total_staff; $i++){
             print $i, " ",$module_ids[$i], " ", $staff_ids[$i], " \n";  
             $dbh->do("INSERT INTO android_module_coordinators (module_id,staff_id) VALUES (?,?)",undef, $module_ids[$i], $staff_ids[$i] );
        }
    }else{
        print "No match between modules and staff!";
    }
}

#get all staff data
select_staff();
#get all modules
select_modules();
#if the number of staff is equal to number of modules each staff teaches only only module(in reality it my not be the case, but for our porpuse it should be okay)
addmodule_staff();

# Disconnect from the database.
$dbh->disconnect();
