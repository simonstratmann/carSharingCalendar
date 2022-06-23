/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 2.30.840 on 2022-03-13 11:15:04.

export interface Config {
  phmavenBranchesToShow?: string[];
  branchesToShowContainingCommit?: string[];
  phmavenBranchToCustomerBranchMap?: { [index: string]: string[] };
  customerProjects?: TphProject[];
  maxRegistrations?: number;
  maxRegistrationsAgeDays?: number;
}

export interface TphProject {
  name?: string;
  projectId?: number;
  branches?: string[];
}

export interface CommitInfo {
  commit?: Commit;
  projectsToContainingBranches?: { [index: string]: string[] };
}

export interface TicketInfo {
  ticketId?: string;
  commitInfos?: CommitInfo[];
  searchedCustomers?: string[];
}

export interface CustomerIntegration {
  branchName?: string;
  evIntegration?: EvIntegration;
  lastCommitHash?: string;
  lastCommitWebUrl?: string;
  lastCommitDate?: string;
  lastCommitTitle?: string;
}

export interface EvIntegration {
  evName?: string;
  evBuildDate?: string;
  evPipelineId?: string;
  evPipelineWebUrl?: string;
  evCommit?: Commit;
}

export interface IntegrationsResponse {
  customerIntegrationMap?: Map<string, CustomerIntegration[]>;
  creationDate?: string;
}

export interface IntegrationsService {
  customerIntegrations?: IntegrationsResponse;
}

export interface RegistrationEntry {
  insertTime?: string;
  sha?: string;
  title?: string;
  tphProjectName?: string;
  branch?: string;
  user?: string;
  notificationTechnology?: NotificationTechnology;
  id?: number;
}

export interface RegistrationMatch {
  registrationEntry?: RegistrationEntry;
  commitInfos?: CommitInfo[];
}

export interface Commit {
  title?: string;
  author?: string;
  sha?: string;
  creationDate?: string;
  webUrl?: string;
}

export type NotificationTechnology = "EMAIL" | "ZULIP";

export type RegistrationResponse = "SUCCESS" | "DUPLICATE";


