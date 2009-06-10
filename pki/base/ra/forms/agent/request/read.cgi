#!/usr/bin/perl
#
# --- BEGIN COPYRIGHT BLOCK ---
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; version 2 of the License.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Copyright (C) 2007 Red Hat, Inc.
# All rights reserved.
# --- END COPYRIGHT BLOCK ---
#
#
#
#

package op;

use lib $ENV{DOCUMENT_ROOT} . "/../lib/perl";

use DBI;
use CGI;
use Template::Velocity;
use PKI::Base::Conf;
use PKI::Base::Registry;
use PKI::Base::Util;
use PKI::Request::Queue;

use vars qw (@ISA);
use PKI::Service::Op;
@ISA = qw(PKI::Service::Op);

sub new {
  my $self = {};
  bless ($self);
  return $self;
}

sub process()
{
  my $self = shift;
  my $q = CGI->new();

  my $util = PKI::Base::Util->new();
  my $docroot = PKI::Base::Registry->get_docroot();
  my $parser = PKI::Base::Registry->get_parser();
  my $cfg = PKI::Base::Registry->get_config();

  $self->debug_params($cfg, $q);

  if (!$self->agent_auth($cfg)) {
    print $q->redirect("/agent/error.cgi");
    return;
  }
  my $uid = $self->get_current_uid($cfg);

  my %context;
  $context{uid} = $util->html_encode($uid);


  my @roles = $self->get_current_roles($cfg);
#  my $r = join(",",@roles);

  my $id = $util->get_alphanum_val($q->param('id'));

  my $queue = PKI::Request::Queue->new();
  $queue->open($cfg);
  my $ref = $queue->read_request_by_roles(\@roles, $id);
  $queue->close();

  $context{data} = $util->breakline($util->html_encode($ref->{'data'}), 40);
  $context{output} = $util->breakline($util->html_encode($ref->{'output'}), 40);
  $context{meta_info} = $util->breakline($util->html_encode($ref->{'meta_info'}), 40);

  $context{serialno} = $util->html_encode($ref->{'serialno'});
  $context{subject_dn} = $util->html_encode($ref->{'subject_dn'});
  $context{type} = $util->html_encode($ref->{'type'});
  $context{created_at} = $util->html_encode($ref->{'created_at'});
  $context{created_by} = $util->html_encode($ref->{'created_by'});
  $context{updated_at} = $util->html_encode($ref->{'updated_at'});
  $context{ip} = $util->html_encode($ref->{'ip'});
  $context{processed_by} = $util->html_encode($ref->{'processed_by'});
  $context{note} = $util->html_encode($ref->{'note'});
  $context{note} =~ s/\n/<br\/>/g;
  $context{assigned_to} = $util->html_encode($ref->{'assigned_to'});
  $context{status} = $util->html_encode($ref->{'status'});
  if ($ref->{'status'} eq "OPEN") {
    $context{is_open} = 1;
  }
  if ($ref->{'status'} eq "ERROR") {
    $context{is_error} = 1;
  }
  $context{errorString} = $util->html_encode($ref->{'errorString'});
  $context{id} = $util->html_encode($ref->{'rowid'});

  my $result = $parser->execute_file_with_context("agent/request/read.vm",
                    \%context);

  my $xml = $q->param('xml');
  if ($xml eq "true") {
    print "Content-Type: text/xml\n\n";
    print $self->xml_output(\%context);
  } else {
    print "Content-Type: text/html\n\n";
    print "$result";
  }
}

my $op = op->new();
$op->execute();
