#!/usr/bin/perl
use strict;
use warnings;
use DBI;
#Description
#This Script has been used to insert a list of staff members to the database
#

my @file_array;
my $dbh;


sub read_file {
open (my $IN, '<', "fakenameStaff.txt" ) or die "Error file cannot be open :$!";
while (my $line = <$IN>) {
    chomp $line;
    my @line_array = split(/\s+/, $line);
    push (@file_array, \@line_array);
}
close $IN || warn $!;
}


sub insert_staff{
    my $array_size=scalar @file_array;
    my $lcfirst_name;
    my $lclast_name;
    my $email;
    my $staffid=1000;

for (my $i=0; $i<$array_size; $i++){
    $lcfirst_name=lc $file_array[$i][0]; #convert to lowercase 
    $lclast_name=lc $file_array[$i][1];  #convert to lowercase
    $email= $lcfirst_name . $lclast_name. '@'."staff.com";
    print $file_array[$i][0], " ", $file_array[$i][1], " ", $email,"\n";
    run_query($staffid,$email, $i);
      $staffid++;
  }
}

sub run_query{
     $dbh->do("INSERT INTO android_staff (staffid,email,first_name,last_name,password,hash) VALUES (?,?,?,?,?,?)",undef, $_[0], $_[1],$file_array[$_[2]][0] ,$file_array[$_[2]][1],"password","0");
}


sub main {
#read file with name
read_file();
# Connect to the database.
$dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});
#insert data to db
insert_staff();
# Disconnect from the database.
$dbh->disconnect();
}

main();
