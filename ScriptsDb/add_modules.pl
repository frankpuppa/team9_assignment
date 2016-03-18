#!/usr/bin/perl
use strict;
use warnings;
use DBI;
#Description
#This Script has been used to insert a list of modules to the database
#

my @file_array;
my $dbh;

sub read_file {
open (my $IN, '<', "fakemodule.txt" ) or die "Error file cannot be open :$!";
while (my $line = <$IN>) {
    chomp $line;
    my @line_array = split(/-/, $line);
    push (@file_array, \@line_array);
}
close $IN || warn $!;
}


sub insert_module{
    my $array_size=scalar @file_array;
    my $moduleid;
for (my $i=0; $i<$array_size; $i++){
    $moduleid=substr ($file_array[$i][0],2);
      print $moduleid, " ", $file_array[$i][0] ," ",$file_array[$i][1],"\n";
       run_query($moduleid, $i);  
  }
}
sub run_query{
$dbh->do("INSERT INTO android_module (moduleid,module_code,module_title) VALUES (?,?,?)",undef, $_[0], $file_array[$_[1]][0] ,$file_array[$_[1]][1]);
}


sub main {
read_file();
# Connect to the database.
$dbh = DBI->connect("DBI:mysql:database=Timetabling;host=5.39.43.115","root", "gUk2yuWSmBfk7SWT",{'RaiseError' => 1});
#insert data into db
insert_module();
# Disconnect from the database.
$dbh->disconnect();

}

main();
